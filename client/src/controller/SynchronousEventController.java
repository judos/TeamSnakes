package controller;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

import model.input.InputProvider;

/**
 * @author Julian Schelker
 */
public class SynchronousEventController implements MouseWheelListener, KeyListener,
	MouseListener, InputProvider {

	private ArrayList<MouseListener> mouseListeners;
	private ArrayList<KeyListener> keyListeners;
	private ConcurrentLinkedQueue<Event> eventQueue;
	private ArrayList<MouseWheelListener> mouseWheelListeners;

	enum InputType {
		mouseClicked, mousePressed, mouseReleased, keyPressed, keyReleased, keyTyped,
		mouseWheelMoved;
	}
	private class Event {
		public InputType type;
		public Object eventObject;

		public Event(InputType type, Object event) {
			this.type = type;
			this.eventObject = event;
		}
	}

	public SynchronousEventController(InputProvider inputProvider) {
		this.keyListeners = new ArrayList<KeyListener>();
		this.mouseListeners = new ArrayList<MouseListener>();
		this.mouseWheelListeners = new ArrayList<MouseWheelListener>();
		this.eventQueue = new ConcurrentLinkedQueue<Event>();
		inputProvider.addKeyListener(this);
		inputProvider.addMouseListener(this);
		inputProvider.addMouseWheelListener(this);
	}

	public void distributeEvents() {
		while (!this.eventQueue.isEmpty()) {
			Event e = this.eventQueue.poll();
			if (e.type == InputType.keyPressed) {
				notifyKeyListeners(KeyListener::keyPressed, e.eventObject);
			}
			else if (e.type == InputType.keyReleased) {
				notifyKeyListeners(KeyListener::keyReleased, e.eventObject);
			}
			else if (e.type == InputType.keyTyped) {
				notifyKeyListeners(KeyListener::keyTyped, e.eventObject);
			}
			else if (e.type == InputType.mouseClicked) {
				notifyMouseListeners(MouseListener::mouseClicked, e.eventObject);
			}
			else if (e.type == InputType.mousePressed) {
				notifyMouseListeners(MouseListener::mousePressed, e.eventObject);
			}
			else if (e.type == InputType.mouseReleased) {
				notifyMouseListeners(MouseListener::mouseReleased, e.eventObject);
			}
			else if (e.type == InputType.mouseWheelMoved) {
				for (MouseWheelListener listener : this.mouseWheelListeners) {
					listener.mouseWheelMoved((MouseWheelEvent) e.eventObject);
				}
			}
		}
	}

	private void notifyMouseListeners(BiConsumer<MouseListener, MouseEvent> method,
		Object eventObject) {
		for (MouseListener o : this.mouseListeners) {
			method.accept(o, (MouseEvent) eventObject);
		}
	}

	private void notifyKeyListeners(BiConsumer<KeyListener, KeyEvent> method,
		Object eventObject) {
		for (KeyListener o : this.keyListeners) {
			method.accept(o, (KeyEvent) eventObject);
		}
	}

	public void addKeyListener(KeyListener listener) {
		this.keyListeners.add(listener);
	}

	public void addMouseListener(MouseListener listener) {
		this.mouseListeners.add(listener);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.eventQueue.add(new Event(InputType.mouseClicked, e));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.eventQueue.add(new Event(InputType.mousePressed, e));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.eventQueue.add(new Event(InputType.mouseReleased, e));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// not recorded
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// not recorded
	}

	@Override
	public void keyTyped(KeyEvent e) {
		this.eventQueue.add(new Event(InputType.keyTyped, e));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.eventQueue.add(new Event(InputType.keyPressed, e));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.eventQueue.add(new Event(InputType.keyReleased, e));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		this.eventQueue.add(new Event(InputType.mouseWheelMoved, e));
	}

	@Override
	public void addMouseWheelListener(MouseWheelListener m) {
		this.mouseWheelListeners.add(m);
	}

}
