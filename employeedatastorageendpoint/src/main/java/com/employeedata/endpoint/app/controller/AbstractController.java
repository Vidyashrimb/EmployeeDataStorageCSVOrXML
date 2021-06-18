/**
 * Abstract controller which handles all the rest exceptions thrown by the controller.
 */

package com.employeedata.endpoint.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.employeedata.endpoint.app.constants.EmployeeDataConstants;
import com.employeedata.endpoint.app.dtos.ResponseDto;


@ControllerAdvice
public abstract class AbstractController {
  // Instance of the Logger
  private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

  // Instance of the ResponseDTO
  private ResponseDto responseDTO = new ResponseDto();

  /**
   * use to handle the Throwable exception, if any exception not handled in code
   * then it would be handle by this method.
   * 
   * @param Throwable - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<String> handleThrowable(final Throwable ex) {
    logger.trace("{} Enter inside :handleThrowable", EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleThrowable {}", EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    responseDTO.setMessage("Failed to process the request");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleThrowable", EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles the exception if handler is found for the request.
   * 
   * @param NoHandlerFoundException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    logger.trace("{} Enter inside :handleNoHandlerFoundException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleNoHandlerFoundException {}",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
    responseDTO.setMessage("No handler found for the provided request");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleNoHandlerFoundException", EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles the HttpRequestMethodNotSupportedException.
   *
   * @param HttpRequestMethodNotSupportedException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
  public ResponseEntity<String> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
    logger.trace("{} Enter inside :handleHttpRequestMethodNotSupported",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleHttpRequestMethodNotSupported {}",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
    responseDTO.setMessage("Method is not allowed for the provided request");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleHttpRequestMethodNotSupported",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.METHOD_NOT_ALLOWED);
  }

  /**
   * Handles the HttpMediaTypeNotSupportedException.
   *
   * @param HttpMediaTypeNotSupportedException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
  public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
    logger.trace("{} Enter inside :handleHttpMediaTypeNotSupportedException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleHttpMediaTypeNotSupportedException {}",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    responseDTO.setMessage("Media type is not supported for the provided request");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleHttpMediaTypeNotSupportedException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  /**
   * Handles the HttpMediaTypeNotAcceptableException.
   *
   * @param HttpMediaTypeNotAcceptableException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
  public ResponseEntity<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
    logger.trace("{} Enter inside :handleHttpMediaTypeNotAcceptableException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleHttpMediaTypeNotAcceptableException {}",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
    responseDTO.setMessage("Media type is not accetable for the provided request");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleHttpMediaTypeNotAcceptableException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.NOT_ACCEPTABLE);
  }

  /**
   * Handles the MissingPathVariableException.
   *
   * @param MissingPathVariableException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({ MissingPathVariableException.class })
  public ResponseEntity<String> handleMissingPathVariableException(MissingPathVariableException ex) {
    logger.trace("{} Enter inside :handleMissingPathVariableException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleMissingPathVariableException {}",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    responseDTO.setMessage("Path variable is missing in the provided request");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleMissingPathVariableException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles the ConversionNotSupportedException.
   *
   * @param ConversionNotSupportedException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({ ConversionNotSupportedException.class })
  public ResponseEntity<String> handleConversionNotSupportedException(ConversionNotSupportedException ex) {
    logger.trace("{} Enter inside :handleConversionNotSupportedException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleConversionNotSupportedException {}",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    responseDTO.setMessage("Property cannot be converted for the provided request");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleConversionNotSupportedException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles the TypeMismatchException.
   *
   * @param TypeMismatchException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ TypeMismatchException.class })
  public ResponseEntity<String> handleTypeMismatchException(TypeMismatchException ex) {
    logger.trace("{} Enter inside :handleTypeMismatchException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleTypeMismatchException {}", EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR,
        ex);

    responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
    responseDTO.setMessage("Property type mismatch for the provided request");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleTypeMismatchException", EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles the HttpMessageNotReadableException.
   *
   * @param HttpMessageNotReadableException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ HttpMessageNotReadableException.class })
  public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    logger.trace("{} Enter inside :handleHttpMessageNotReadableException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleHttpMessageNotReadableException {}",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
    responseDTO.setMessage("Invalid PayLoad. AppNames should be any one of CD, EDL, CSU");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleHttpMessageNotReadableException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.BAD_REQUEST);
  }
  
  /**
   * Handles the HttpMessageNotWritableException.
   *
   * @param HttpMessageNotWritableException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({ HttpMessageNotWritableException.class })
  public ResponseEntity<String> handleHttpMessageNotWritableException(HttpMessageNotWritableException ex) {
    logger.trace("{} Enter inside :handleHttpMessageNotWritableException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleHttpMessageNotWritableException {}",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    responseDTO.setMessage("Provided request is not in the writable format");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleHttpMessageNotWritableException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles the MethodArgumentNotValidException.
   *
   * @param MethodArgumentNotValidException - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ MethodArgumentNotValidException.class })
  public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    logger.trace("{} Enter inside :handleMethodArgumentNotValidException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleMethodArgumentNotValidException {}",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
    responseDTO.setMessage("Argument is invalid");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleMethodArgumentNotValidException",
        EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles the generic exception.
   *
   * @param Exception - name of the exception
   * @return Returns a json format message to the user
   */
  @ResponseStatus(HttpStatus.SEE_OTHER)
  @ExceptionHandler({ Exception.class })
  public ResponseEntity<String> handleException(Exception ex) {
    logger.trace("{} Enter inside :handleException", EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    logger.error("{} Exception :handleException {}", EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR, ex);

    responseDTO.setStatusCode(HttpStatus.SEE_OTHER.value());
    responseDTO.setMessage("Failed to process the request");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    logger.trace("{} Exit from :handleException", EmployeeDataConstants.LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR);

    return new ResponseEntity<>(responseDTO.toString(), headers, HttpStatus.SEE_OTHER);
  }
}
