package com.muse.router.compiler.processor;

import com.google.auto.service.AutoService;
import com.muse.router.facade.enums.NodeType;
import com.muse.router.facade.model.Node;
import com.muse.router.compiler.utils.Logger;
import com.muse.router.facade.utils.RouterUtils;
import com.muse.router.compiler.utils.TypeUtils;
import com.muse.router.facade.annotation.Autowired;
import com.muse.router.facade.annotation.Route;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.muse.router.compiler.utils.Constants.ACTIVITY;
import static com.muse.router.compiler.utils.Constants.ANNOTATION_TYPE_AUTOWIRED;
import static com.muse.router.compiler.utils.Constants.ANNOTATION_TYPE_ROUTE;
import static com.muse.router.compiler.utils.Constants.BASECOMPROUTER;
import static com.muse.router.compiler.utils.Constants.KEY_HOST_NAME;
import static com.muse.router.compiler.utils.Constants.STRING;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by GuoWee on 2018/4/26.
 */
@AutoService(Processor.class)
@SupportedOptions(KEY_HOST_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({ANNOTATION_TYPE_ROUTE, ANNOTATION_TYPE_AUTOWIRED})
public class RouterProcessor extends AbstractProcessor {
    private static final String mRouteMapperFieldName = "routeMapper";
    private static final String mParamsMapperFieldName = "paramsMapper";
    private Filer mFiler;
    private Elements elements;
    private Types types;

    private Logger logger;

    private List<Node> routeNodes;
    private TypeUtils typeUtils;

    private String host = null;

    private TypeMirror typeString;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        routeNodes = new ArrayList<>();
        mFiler = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elements = processingEnv.getElementUtils();
        logger = new Logger(processingEnv.getMessager());
        typeUtils = new TypeUtils(types, elements);
        typeString = elements.getTypeElement(STRING).asType();

        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            host = options.get(KEY_HOST_NAME);
            logger.info(">>> host is " + host + " <<<");
        }
        if (host == null || host.equals("")) {
            host = "default";
        }
        logger.info(">>> RouteProcessor init. <<<");


    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (CollectionUtils.isNotEmpty(annotations)) {
            Set<? extends Element> routes = roundEnv.getElementsAnnotatedWith(Route.class);
            try {
                logger.info(">>> Found routes, start! ");
                parseRoutes(routes);
            } catch (Exception e) {
                logger.error(e);
            }

            brewJavaForRouterImpl();

        }
        return false;
    }

    private void parseRoutes(Set<? extends Element> routes) {
        TypeMirror typeActivity = elements.getTypeElement(ACTIVITY).asType();
        for (Element element : routes) {
            TypeMirror tm = element.asType();
            Route route = element.getAnnotation(Route.class);
            if (types.isSubtype(tm, typeActivity)) {  // Activity
                logger.info(">>> Found activity route: " + tm.toString() + " <<<");
                Node node = new Node();
                String path = route.path();
                checkPath(path);

                node.setPath(path);
                node.setDesc(route.desc());
                node.setNodeType(NodeType.ACTIVITY);
                node.setRawType(element);

                Map<String, Integer> paramsType = new HashMap<>();
                Map<String, String> paramsDesc = new HashMap<>();

                for (Element field : element.getEnclosedElements()) {
                    if (field.getKind().isField() && field.getAnnotation(Autowired.class) != null) {
                        Autowired paramConfig = field.getAnnotation(Autowired.class);

                        paramsType.put(StringUtils.isEmpty(paramConfig.name())
                                ? field.getSimpleName().toString() : paramConfig.name(), typeUtils.typeExchange(field));
                        paramsDesc.put(StringUtils.isEmpty(paramConfig.desc())
                                ? field.getSimpleName().toString() : paramConfig.desc(), typeUtils.typeDesc(field));
                    }
                }
                node.setParamsType(paramsType);
                node.setParamsDesc(paramsDesc);

                if (!routeNodes.contains(node)) {
                    routeNodes.add(node);
                }
            } else {
                throw new IllegalStateException("only activity can be annotated by Route");
            }
        }
    }


    private void checkPath(String path) {
        if (path == null || path.isEmpty() || !path.startsWith("/")) {
            throw new IllegalArgumentException("path cann't be null or empty, and should start with /, this is " + path);
        }
        if (path.contains("//") || path.contains("&") || path.contains("?")) {
            throw new IllegalArgumentException("path should not contain //, & or ?,this is " + path);
        }

        if (path.endsWith("/")) {
            throw new IllegalArgumentException("path should not endWith /, this is " + path + "; or append a token:index");
        }
    }


    private void brewJavaForRouterImpl() {
        // class name
        String clsName = RouterUtils.genHostUIRouterClass(host);
        // package name
        String pkgName = clsName.substring(0, clsName.lastIndexOf("."));
        // simple name
        String cn = clsName.substring(clsName.lastIndexOf(".") + 1);
        // superClassName
        ClassName superClass = ClassName.get(elements.getTypeElement(BASECOMPROUTER));
        MethodSpec initHostMethod = generateInitHostMethod();
        MethodSpec initMapMethod = generateInitMapMethod();

        try {
            JavaFile.builder(pkgName, TypeSpec.classBuilder(cn)
                    .addModifiers(PUBLIC)
                    .superclass(superClass)
                    .addMethod(initHostMethod)
                    .addMethod(initMapMethod)
                    .build()
            ).build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private MethodSpec generateInitHostMethod() {
        TypeName returnType = TypeName.get(typeString);

        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("getHost")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        openUriMethodSpecBuilder.addStatement("return $S", host);

        return openUriMethodSpecBuilder.build();
    }

    private MethodSpec generateInitMapMethod() {
        TypeName returnType = TypeName.VOID;

        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("initMap")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        openUriMethodSpecBuilder.addStatement("super.initMap()");
        for (Node node : routeNodes) {
            openUriMethodSpecBuilder.addStatement(
                    mRouteMapperFieldName + ".put($S,$T.class)",
                    node.getPath(),
                    ClassName.get((TypeElement) node.getRawType()));

            // Make map body for paramsType
            StringBuilder mapBodyBuilder = new StringBuilder();
            Map<String, Integer> paramsType = node.getParamsType();
            if (MapUtils.isNotEmpty(paramsType)) {
                for (Map.Entry<String, Integer> types : paramsType.entrySet()) {
                    mapBodyBuilder.append("put(\"").append(types.getKey()).append("\", ").append(types.getValue()).append("); ");
                }
            }
            String mapBody = mapBodyBuilder.toString();
            logger.info(">>> MapBody: " + mapBody + " <<<");
            if (!StringUtils.isEmpty(mapBody)) {
                openUriMethodSpecBuilder.addStatement(
                        mParamsMapperFieldName + ".put($T.class,"
                                + "new java.util.HashMap<String, Integer>(){{" + mapBody + "}}" + ")",
                        ClassName.get((TypeElement) node.getRawType()));
            }
        }

        return openUriMethodSpecBuilder.build();


    }


}










