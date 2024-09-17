package com.nhce.saloni.smartEducation.authentication;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;

public class AuthenticationFirebase {

    private static AuthenticationFirebase authenticationFirebase = null;

    private FirebaseAuth firebaseAuth;
    private AuthStateListener authStateListener;

    private AuthenticationFirebase(){
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.authStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    public FirebaseAuth getFirebaseAuth() {
        return this.firebaseAuth;
    }

    public AuthStateListener getAuthStateListener(){
        return this.authStateListener;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public void setAuthStateListener(AuthStateListener authStateListener){
        this.authStateListener = authStateListener;
    }

    public static AuthenticationFirebase getInstance(){
        return authenticationFirebase == null? new AuthenticationFirebase(): authenticationFirebase;
    }

    public void attachListener(){
        AuthenticationFirebase.getInstance().getFirebaseAuth().addAuthStateListener(authStateListener);
    }

    public void removeListener(){
        AuthenticationFirebase.getInstance().getFirebaseAuth().removeAuthStateListener(authStateListener);
    }
}
