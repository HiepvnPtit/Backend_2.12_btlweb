package javaSpring.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javaSpring.dto.response.APIResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    //Bắt tất cả các Exception chưa được xử lý
    @ExceptionHandler(value= Exception.class)
    ResponseEntity<APIResponse> handlingRuntimeException(RuntimeException exception){
        APIResponse apiResponse = new APIResponse<>();

        apiResponse.setCode(ErrorCode.UNCATEGORIZE_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZE_EXCEPTION.getMessage());

        //in lỗi
        exception.printStackTrace();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    //Bắt các AppException
    @ExceptionHandler(value= AppException.class)
    ResponseEntity<APIResponse> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        APIResponse apiResponse = new APIResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        
        return ResponseEntity.badRequest().body(apiResponse);
    }


    //Bắt các lỗi validate @Valid
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handlingRuntimeException(MethodArgumentNotValidException exception){
        String enumkey = exception.getFieldError().getDefaultMessage();  //message
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        errorCode = ErrorCode.valueOf(enumkey); //convert String -> Enum
        
        APIResponse apiResponse = new APIResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
