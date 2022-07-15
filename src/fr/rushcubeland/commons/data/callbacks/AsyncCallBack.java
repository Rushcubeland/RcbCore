package fr.rushcubeland.commons.data.callbacks;

public interface AsyncCallBack {

    void onQueryComplete(Object result);

    void onQueryError(Exception e);
}
