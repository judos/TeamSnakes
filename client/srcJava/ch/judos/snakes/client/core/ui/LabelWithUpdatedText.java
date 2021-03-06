package ch.judos.snakes.client.core.ui;

import java.awt.*;
import java.util.function.Supplier;


public class LabelWithUpdatedText extends Label {

	private Supplier<String> supplier;

	public LabelWithUpdatedText(Supplier<String> supplier, boolean isTitle) {
		super(supplier.get(), isTitle);
		this.supplier = supplier;
	}
	
	@Override
	public void render(Graphics g) {
		this.title = this.supplier.get();
		super.render(g);
	}
	
	@Override
	protected String getTextForSizeCalculation() {
		return this.supplier.get();
	}
}
