package com.example.auth.commons.decorator;

import com.example.auth.commons.constant.ResponseConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@ToString
public class Response {
    int code;
    String status;
    String description;

    public Response(int ok, String ok1, String okDescription) {
        this.code = ok;
        this.status = ok1;
        this.description = okDescription;
    }

    public static Response getOkResponse(String message) {
        return new Response(HttpStatus.OK.value(), ResponseConstant.OK,message);

    }

    public static Response getEmptyResponse(String message) {
        return new Response(HttpStatus.NOT_FOUND.value(),ResponseConstant.OK, message);

    }

    public static Response getInvaildResponse(String message) {
        return new Response(HttpStatus.BAD_REQUEST.value(),ResponseConstant.OK,message);

    }

    public static Response getAlreadyExists(String message) {
        return new Response(HttpStatus.BAD_REQUEST.value(),ResponseConstant.OK, message);

    }

    public static Response getInvalidRequestException(String message) {
        return new Response(HttpStatus.BAD_REQUEST.value(),ResponseConstant.OK, message);
    }

    public static Response getNotFoundResponse(String message) {
        return new Response(HttpStatus.BAD_REQUEST.value(),ResponseConstant.OK, message);
    }

    public static Response getOkResponse() {
        return new Response(HttpStatus.OK.value(), ResponseConstant.OK,ResponseConstant.OK);
    }

    public static Response getInternalServerErrorResponse() {
        return new Response(HttpStatus.BAD_REQUEST.value(), ResponseConstant.OK,ResponseConstant.OK);
    }

    public static Response getUpdateResponse() {
        return new Response(HttpStatus.OK.value(), ResponseConstant.UPDATED,ResponseConstant.OK);
    }

        public Response getResponse(HttpStatus httpStatus,Object constant,Object constant1){
            return new Response(httpStatus.value(), constant.toString(),constant1.toString());
        }
    }



