package com.xwm.magicmaid.enumstorage;

public enum  EnumInstructElement
{
    NONE,
    CONTENT,
    ITEM,
    ENTITY,
    TITLE,
    IMAGE,
    FORMULA,
    RECIPE;

    public static EnumInstructElement parse(String type)
    {
        if (type == null)
            return NONE;
        else if (type.equals("content"))
            return CONTENT;
        else if (type.equals("item"))
            return ITEM;
        else if (type.equals("title"))
            return TITLE;
        else if (type.equals("image"))
            return IMAGE;
        else if (type.equals("entity"))
            return ENTITY;
        else if (type.equals("formula"))
            return FORMULA;
        else if (type.equals("recipe"))
            return RECIPE;
        return NONE;
    }
}
