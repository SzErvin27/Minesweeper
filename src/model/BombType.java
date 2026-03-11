package model;

/**
 * A BombType a bombák típusait határozza meg
 */
public enum BombType {
	NONE(0),     // nincs bomba
    FAKE(0),     // "álbomba": 0 sebzés
    WEAK(1),     // gyenge bomba: 1 életet vesz le
    MEDIUM(2),   // közepes bomba: 2 életet vesz le
    STRONG(3);   // erős bomba: 3 életet vesz le

    private final int damage;

    /**
     * BombType konstruktor
     *
     * @param damage: a bomba sebzése
     */
    BombType(int damage) {
        this.damage = damage;
    }

    /**
     * Visszaadja a bomba sebzését
     *
     * @return: a bomba sebzése
     */
    public int getDamage() {
        return damage;
    }
}
