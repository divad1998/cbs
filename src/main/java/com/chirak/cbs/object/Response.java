package com.chirak.cbs.object;

public class Response {
    private boolean status;



    private String message;
    private Object data;

    public Response(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
