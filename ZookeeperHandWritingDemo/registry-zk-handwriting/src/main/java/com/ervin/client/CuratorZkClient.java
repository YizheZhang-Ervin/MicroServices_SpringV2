package com.ervin.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.ervin.consumer.ChildListener;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.listen.Listenable;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class CuratorZkClient implements ZkClient{
	
	private CuratorFramework client;
	
	public CuratorZkClient(String url) {
		RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
		client = CuratorFrameworkFactory.newClient(url, 5000, 3000,retry);
		client.start();
	}

	public void create(String url, boolean ephemeral) throws Exception {
		CreateMode mode = ephemeral ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT;
		client.create().creatingParentsIfNeeded().withMode(mode).forPath(url);

	}

	public void delete(String url) throws Exception {
		client.delete().forPath(url);
	}

	public void watchChild(String url, ChildListener listener) throws Exception {
		CuratorCache cache = CuratorCache.build(client,url,CuratorCache.Options.SINGLE_NODE_CACHE);
		cache.listenable().addListener(listener);
		cache.start();

	}

	public List<String> getChild(String url) throws Exception {
		return client.getChildren().forPath(url);

	}
	
	

}
