package com.adam.webapp.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import javax.ws.rs.NameBinding;

/*
 * The @Secured annotation is defined in this interface. It is used to bind
 * the AuthenticationFilter to resource methods found in AppEntryPoints class
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Secured { }