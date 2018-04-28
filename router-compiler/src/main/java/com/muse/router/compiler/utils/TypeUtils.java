package com.muse.router.compiler.utils;


import com.muse.router.facade.enums.Type;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


import static com.muse.router.compiler.utils.Constants.PARCELABLE;
import static com.muse.router.compiler.utils.Constants.SERIALIZABLE;

/**
 * Created by GuoWee on 2018/4/27.
 */

public class TypeUtils {

    private Types types;
    private Elements elements;
    private TypeMirror parcelableType;
    private TypeMirror serializableType;

    public TypeUtils(Types types, Elements elements) {
        this.types = types;
        this.elements = elements;
        parcelableType = this.elements.getTypeElement(PARCELABLE).asType();
        serializableType = this.elements.getTypeElement(SERIALIZABLE).asType();
    }

    public int typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case Constants.BYTE:
                return Type.BYTE.ordinal();
            case Constants.SHORT:
                return Type.SHORT.ordinal();
            case Constants.INTEGER:
                return Type.INT.ordinal();
            case Constants.DOUBLE:
                return Type.DOUBLE.ordinal();
            case Constants.FLOAT:
                return Type.FLOAT.ordinal();
            case Constants.LONG:
                return Type.LONG.ordinal();
            case Constants.BOOLEAN:
                return Type.BOOLEAN.ordinal();
            case Constants.STRING:
                return Type.STRING.ordinal();
            default:
                if (types.isSubtype(typeMirror, parcelableType)) {  // PARCELABLE
                    return Type.PARCELABLE.ordinal();
                } else if (types.isSubtype(typeMirror, serializableType)) { // SERIALIZABLE
                    return Type.SERIALIZABLE.ordinal();
                } else {    // For others
                    return Type.OBJECT.ordinal();
                }


        }

    }

    public String typeDesc(Element element) {
        TypeMirror typeMirror = element.asType();

        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().name();
        }

        switch (typeMirror.toString()) {
            case Constants.BYTE:
                return "byte";
            case Constants.SHORT:
                return "short";
            case Constants.INTEGER:
                return "int";
            case Constants.LONG:
                return "long";
            case Constants.FLOAT:
                return "byte";
            case Constants.DOUBLE:
                return "double";
            case Constants.BOOLEAN:
                return "boolean";
            case Constants.STRING:
                return "String";
            default:    // Other side, maybe the PARCELABLE or OBJECT.
                if (types.isSubtype(typeMirror, parcelableType)) {  // PARCELABLE
                    return "parcelable";
                } else if (types.isSubtype(typeMirror, serializableType)) {// SERIALIZABLE
                    return "serializable";
                } else {    // For others
                    return typeMirror.toString();
                }
        }
    }


}
