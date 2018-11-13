package livestream.models;

import java.io.Serializable;

public class BaseRequest<T extends Serializable> implements Serializable {

    private int mTypeRequest;
    private String mMessage;
    private T mData;

    public BaseRequest(int typeRequest, String message, T data) {
        mTypeRequest = typeRequest;
        mMessage = message;
        mData = data;
    }

    public int getTypeRequest() {
        return mTypeRequest;
    }

    public void setTypeRequest(int typeRequest) {
        mTypeRequest = typeRequest;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }
}
