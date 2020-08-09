package fr.evywell.robgame.world.character;

public class InvokeCharacterInWorldPayload {

    public int character_uuid;

    public InvokeCharacterInWorldPayload() {}

    public InvokeCharacterInWorldPayload(int character_uuid) {
        this.character_uuid = character_uuid;
    }

}
