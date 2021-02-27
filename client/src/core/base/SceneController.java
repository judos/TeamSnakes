package core.base;

public interface SceneController {

	public Scene loadScene(Class<? extends Scene> scene, Object... arg);

	public void requestScreenshot();
}
