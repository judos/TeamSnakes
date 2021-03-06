package ch.judos.snakes.client.core.base;

public interface NamedComponent {

	default String getName() {
		return this.getClass().getSimpleName();
	}
	
}
