/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jcertif.android.fragments;

import android.os.Bundle;

import com.jcertif.android.JcertifApplication;

/**
 * @author Komi Serge Innocent <komi.innocent@gmail.com>
 */
public interface RestFragment {

    public JcertifApplication getApplicationContext();

    /**
     * Implementers of this Fragment will handle the result here.
     * @param code
     * @param result
     */
    public void onRESTResult(int code, Bundle result);
}
