package com.wl.instamer.validation;

import java.util.List;

public interface Validator<T> {
	public List<String> validate(T obj);
}
