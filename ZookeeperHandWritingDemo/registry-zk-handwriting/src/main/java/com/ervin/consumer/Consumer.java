package com.ervin.consumer;

import java.util.HashSet;
import java.util.Set;

import com.ervin.Entity;
import com.ervin.registry.ZookeeperRegistry;
import org.apache.curator.framework.recipes.cache.ChildData;

public class Consumer implements ChildListener {

	private Set<String> providers;

	private Entity entity;

	public Consumer(String service, String node) {
		this.entity = new Entity();
		this.entity.setService(service);
		this.entity.setNode(node);
		this.entity.setType(ZookeeperRegistry.CONSUMER);
		this.providers = new HashSet<>();
	}

	public void printProviders() {
		System.out.println("------------->Print providres by consumer:start<----------");
		for (String url : providers) {
			System.out.println(url);
		}
		System.out.println("------------->Print providres by consumer:end<----------");
	}

	public Set<String> getProviders() {
		return providers;
	}

	public Entity getEntity() {
		return entity;
	}

	@Override
	public void event(Type type, ChildData childData, ChildData childData1) {
		switch (type) {
			case NODE_CREATED:
				// System.out.println("CHILD_ADD," + event.getData().getPath());
				providers.add(childData.getPath());
				break;
			case NODE_CHANGED:
				// System.out.println("CHILD_UPDATEED" + event.getData().getPath());
				providers.add(childData.getPath());
				break;
			case NODE_DELETED:
				// System.out.println("CHILD_REMOVED," + event.getData().getPath());
				providers.remove(childData.getPath());
				break;
			default:
				break;
		}
	}

	@Override
	public void initialized() {

	}
}
