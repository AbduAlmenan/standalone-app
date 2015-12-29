/*
 * Copyright 2015 Sam Sun <me@samczsun.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.samczsun.helios.handler.files;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Control;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public abstract class FileHandler {
    private static final Map<String, FileHandler> BY_ID = new HashMap<>();

    static {
        new JavaHandler().register();
        new CatchAllHandler().register();
    }

    public static final FileHandler ANY = BY_ID.get("Hex");

    public final FileHandler register() {
        if (!BY_ID.containsKey(getId())) {
            BY_ID.put(getId(), this);
        }
        return this;
    }

    public abstract Control generateTab(CTabFolder parent, String file, String name);

    public abstract boolean accept(String name);

    public static Collection<FileHandler> getAllHandlers() {
        return BY_ID.values();
    }

    public abstract String getId();
}
