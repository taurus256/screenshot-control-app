package ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration;

public enum Resolution {
			_1920(1920, 1080), // не поддерживается фронтом и агентами! оставлено для совместимости с БД
			_1900(1900, 1080),
			_1600(1600, 1200),
			_1280(1280,1024),
			_1024(1024,768),
			_768(768,1024);
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
