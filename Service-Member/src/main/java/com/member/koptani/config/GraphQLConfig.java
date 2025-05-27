package com.member.koptani.config;

import com.member.koptani.exception.MemberException;
import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.Date); // can more
    }

    @Bean
    public DataFetcherExceptionResolverAdapter validationExceptionResolver() {
        return new DataFetcherExceptionResolverAdapter() {
            @Override
            protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
                // MethodArgumentNotValidException & BindException
                if (ex instanceof MethodArgumentNotValidException manv) {
                    return buildValidationError(manv.getBindingResult().getFieldErrors(), env);
                }
                if (ex instanceof BindException bindEx) {
                    return buildValidationError(bindEx.getFieldErrors(), env);
                }
                if (ex instanceof MemberException me) {
                    return GraphqlErrorBuilder.newError(env)
                            .message(me.getMessage())
                            .errorType(ErrorClassification.errorClassification("BAD_REQUEST"))
                            .build();
                }
                // ConstraintViolationException
                if (ex instanceof ConstraintViolationException cve) {
                    List<Map<String,String>> errors = cve.getConstraintViolations().stream()
                            .map(violation -> {
                                // Ambil nama field terakhir dari property path
                                String fieldName = "";
                                for (var node : violation.getPropertyPath()) {
                                    fieldName = node.getName();
                                }
                                return Map.of(fieldName, violation.getMessage());
                            })
                            .collect(Collectors.toList());

                    return GraphqlErrorBuilder.newError(env)
                            .message("Validation failed")
                            .errorType(ErrorType.ValidationError)
                            .extensions(Map.of("validationErrors", errors))
                            .build();
                }

                return null;
            }

            private GraphQLError buildValidationError(List<FieldError> fieldErrors, DataFetchingEnvironment env) {
                List<Map<String,String>> errors = fieldErrors.stream()
                        .map(fe -> Map.of(
                                "field", fe.getField(),
                                "message", fe.getDefaultMessage()
                        ))
                        .collect(Collectors.toList());

                return GraphqlErrorBuilder.newError(env)
                        .message("Validation failed")
                        .errorType(ErrorType.ValidationError)
                        .extensions(Map.of("validationErrors", errors))
                        .build();
            }
        };
    }
}