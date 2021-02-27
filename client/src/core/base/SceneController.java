package core.base;

public interface SceneController {

	public Scene loadScene(Class<? extends Scene> scene);

	public void requestScreenshot();

	public void quit();
}
