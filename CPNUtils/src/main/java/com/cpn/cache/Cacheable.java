package com.cpn.cache;

import java.io.Serializable;

public interface Cacheable<T> extends Serializable {

	public T getKey();

}
