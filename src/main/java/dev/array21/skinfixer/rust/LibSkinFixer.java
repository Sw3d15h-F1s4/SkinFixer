package dev.array21.skinfixer.rust;

public class LibSkinFixer {

	protected static native void init(String type, String host, String database, String username, String password);
	protected static native String[] getSkinProfile(String uuid);
	protected static native void setSkinProfile(String uuid, String value, String signature);
	protected static native void delSkinProfile(String uuid);
}
