package com.cpn.cache;

import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhcacheWrapper<K, V extends Cacheable<K>> implements CacheWrapper<K, V> {
	private final CacheManager cacheManager;
	private final String cacheName;

	public EhcacheWrapper(final String cacheName, final CacheManager aManager) {
		this.cacheName = cacheName;
		this.cacheManager = aManager;
		if(!cacheManager.cacheExists(cacheName)){
			cacheManager.addCache(cacheName);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(final K key) {
		final Element element = getCache().get(key);
		if (element != null) {
			return (V) element.getValue();
		}
		return null;
	}

	public Ehcache getCache() {
		return cacheManager.getEhcache(cacheName);
	}

	@Override
	public CacheWrapper<K, V> put(final K key, final V value) {
		getCache().put(new Element(key, value));
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<K> getKeys() {
		return getCache().getKeys();
	}

	@Override
	public CacheWrapper<K, V> putAll(final List<V> aList) {
		for (final V v : aList) {
			put(v.getKey(), v);
		}
		return this;
	}

	@Override
	public CacheWrapper<K, V> removeAll() {
		cacheManager.getEhcache(cacheName).removeAll();
		return this;
	}
}