package com.deendayalproject.uidai.crypto;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {CryptLibModule.class})
public interface CryptoStrategy {

    String encrypt(String body) throws Exception;

    String decrypt(String data) throws Exception;
}


