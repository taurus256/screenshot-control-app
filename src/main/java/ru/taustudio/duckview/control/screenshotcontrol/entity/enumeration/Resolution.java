package ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration;

public enum Resolution {
			_1920x1080 (1920, 1080),
			_1280x1024(1280,1024),
			_1024x768(1024,768),
			_768x1024(768,1024);
	private int width;
	private int height;
	Resolution(int width, int height){
		this.width=width;
		this.height = height;
	}
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
