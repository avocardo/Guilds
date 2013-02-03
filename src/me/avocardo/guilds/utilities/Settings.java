package me.avocardo.guilds.utilities;

public enum Settings {

	ENABLE_CHANGE_GUILD(false),
	ENABLE_NO_GUILD(true),
	ENABLE_DEFAULT_GUILD(false),
	ENABLE_FRIENDLY_FIRE_PVP(true),
	ENABLE_BASE_ON_DEATH(true),
	ENABLE_GUILD_PROTECTION_BARRIER(false),
	ENABLE_GUILD_PROTECTION_BARRIER_VOID(false),
	ENABLE_ENEMY_ENTER_PROTECTION_BARRIER(false),
	ENABLE_DAMAGE_ANIMATION_ON_ZERO(true),
	ENABLE_CHAT_COLOR(true),
	ENABLE_PLAYER_GUILD_PREFIX(true),
	ENABLE_PLAYER_LISTNAME_COLOR(false),
	ENABLE_PLAYER_LISTNAME_GUILD(false),
	ENABLE_GUILD_CHAT_FORMAT(false),
	ENABLE_GUILD_JOIN_PERMISSIONS(false),
	ENABLE_RESTRICTIONS(true),
	ENABLE_PICKUP_RESTRICTIONS(false),
	ENABLE_GUILD_NAME_ON_BROADCAST(false),
	SET_PROTECTION_BARRIER(50),
	SET_BASE_TP_DELAY(100),
	SET_CHAT_PREFIX("<"),
	SET_CHAT_SUFFIX(">"),
	SET_GUILDS_BROADCAST_COLOR("&d"),
	SET_DEFAULT_GUILD("Test"),
	SET_CHANGE_GUILD_TIME(0),
	SET_PROTECTION_BARRIER_VOID(0);
	
	private Object Setting;
	
	Settings(Object Setting) {
		this.Setting = Setting;
	}
	
	public boolean findSettings(String str) {
		if (this.toString().equalsIgnoreCase(str)) return true;
		return false;
	}
	
	public void setSetting(Object Setting) {
		this.Setting = Setting;
	}
	
	public Object getSetting() {
		return this.Setting;
	}
	
	public boolean isBoolean() {
		switch (this) {
		case ENABLE_CHANGE_GUILD:
		case ENABLE_NO_GUILD:
		case ENABLE_DEFAULT_GUILD:
		case ENABLE_FRIENDLY_FIRE_PVP:
		case ENABLE_BASE_ON_DEATH:
		case ENABLE_GUILD_PROTECTION_BARRIER:
		case ENABLE_GUILD_PROTECTION_BARRIER_VOID:
		case ENABLE_ENEMY_ENTER_PROTECTION_BARRIER:
		case ENABLE_DAMAGE_ANIMATION_ON_ZERO:
		case ENABLE_CHAT_COLOR:
		case ENABLE_PLAYER_GUILD_PREFIX:
		case ENABLE_PLAYER_LISTNAME_COLOR:
		case ENABLE_PLAYER_LISTNAME_GUILD:
		case ENABLE_GUILD_CHAT_FORMAT:
		case ENABLE_GUILD_JOIN_PERMISSIONS:
		case ENABLE_RESTRICTIONS:
		case ENABLE_PICKUP_RESTRICTIONS:
		case ENABLE_GUILD_NAME_ON_BROADCAST:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isString() {
		switch (this) {
		case SET_CHAT_PREFIX:
		case SET_CHAT_SUFFIX:
		case SET_GUILDS_BROADCAST_COLOR:
		case SET_DEFAULT_GUILD:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isLong() {
		switch (this) {
		case SET_CHANGE_GUILD_TIME:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isInteger() {
		switch (this) {
		case SET_PROTECTION_BARRIER:
		case SET_BASE_TP_DELAY:
		case SET_PROTECTION_BARRIER_VOID:
			return true;
		default:
			return false;
		}
	}
	
}
