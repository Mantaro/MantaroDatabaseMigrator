/*
 * Copyright (C) 2016 Kodehawa
 *
 * Mantaro is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Mantaro is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mantaro. If not, see http://www.gnu.org/licenses/
 *
 */

package net.kodehawa.migrator.mongodb;

import net.kodehawa.migrator.Migrator;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class CustomCommand implements ManagedMongoObject {
    @BsonIgnore
    public static final String DB_TABLE = "commands";

    private String id;
    private String guildId;
    private List<String> values;
    private String owner;
    private boolean nsfw;
    private boolean locked;

    @BsonCreator
    public CustomCommand(@BsonId String id, @BsonProperty("guildId") String guildId, @BsonProperty("values") List<String> values,
                         @BsonProperty("owner") String owner, @BsonProperty("nsfw") boolean nsfw, @BsonProperty("locked") boolean locked) {
        this.id = id;
        this.guildId = guildId;
        this.values = values.stream().map(Migrator::decodeURL).collect(Collectors.toList());
        this.owner = owner;
        this.nsfw = nsfw;
        this.locked = locked;
    }

    @BsonIgnore
    public static CustomCommand of(String guildId, String cmdName, List<String> responses) {
        return new CustomCommand(guildId + ":" + cmdName, guildId, responses.stream().map(Migrator::encodeURL).collect(Collectors.toList()),
                "", false, false);
    }

    @BsonIgnore
    public static CustomCommand transfer(String guildId, CustomCommand command) {
        return new CustomCommand(guildId + ":" + command.getName(), guildId, command.getValues(),
                command.getOwner(), command.isNsfw(), command.isLocked());
    }

    @BsonIgnore
    public String getName() {
        return getId().split(":", 2)[1];
    }

    @Override
    public String getId() {
        return id;
    }

    public List<String> getValues() {
        return values;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @BsonIgnore
    @Override
    public String getTableName() {
        return DB_TABLE;
    }

    @BsonIgnore
    @Override
    public void save() {
        Migrator.saveMongo(this, CustomCommand.class);
    }

    @BsonIgnore
    @Override
    public void delete() {
        Migrator.deleteMongo(this, CustomCommand.class);
    }
}
