package fzzfegg.com.yunguild.Sql;

public enum SQLCommand {
    //创建lists
    CREATE_TABLE1(
            "CREATE TABLE IF NOT EXISTS `guild_lists` (" +
                    "`id` int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`guild_name` VARCHAR(100) NULL DEFAULT NULL," +
                    "`admin_name` VARCHAR(18) NULL DEFAULT NULL," +
                    "`max_limit` INT(3) NULL DEFAULT NULL," +
                    "`guild_level` INT(3) DEFAULT 1," +
                    "`guild_chest` INT(3) DEFAULT 0," +
                    "`guild_info` varchar(255) DEFAULT '说点什么把。'," +
                    "`flag_pvp` VARCHAR(6) DEFAULT 'false'," +
                    "`flag_tpall` VARCHAR(6) DEFAULT 'false'," +
                    "`tp` VARCHAR(100)," +
                    "`guild_title` varchar(255))"
    ),
    CREATE_TABLE2(
            "CREATE TABLE IF NOT EXISTS `guild_list` (" +
                    "`id` int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`player_name` VARCHAR(18) DEFAULT NULL," +
                    "`identity` VARCHAR(30) DEFAULT 'default'," +
                    "`guild_name` VARCHAR(100) NULL DEFAULT NULL)"
    ),
    CREATE_TABLE3(
            "CREATE TABLE IF NOT EXISTS `apply` (" +
                    "`id` int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`player_name` VARCHAR(18) NULL DEFAULT NULL," +
                    "`guild_name` VARCHAR(100) NULL DEFAULT NULL)"
    ),
    CREATE_TABLE4(
            "CREATE TABLE IF NOT EXISTS `sign` (" +
                    "`id` int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`player_name` VARCHAR(18) DEFAULT NULL," +
                    "`oldTime` TIMESTAMP NULL DEFAULT NULL," +
                    "`onlineTime` int(12) NULL DEFAULT NULL," +
                    "`count` int(3) DEFAULT 1," +
                    "`guild_name` VARCHAR(100) NULL DEFAULT NULL)"
    ),


    ADD_DATA1(
            "INSERT INTO `guild_lists` " +
                    "(`guild_name`,`admin_name`,`max_limit`)" +
                    "VALUES (?,?,?)"
    ),
    ADD_DATA2(
            "INSERT INTO `guild_list` " +
                    "(`player_name`,`identity`,`guild_name`)" +
                    "VALUES (?,?,?)"
    ),
    ADD_DATA3(
            "INSERT INTO `apply` " +
                    "(`player_name`,`guild_name`)" +
                    "VALUES (?,?)"
    ),
    ADD_TIME(
            "INSERT INTO `sign` " +
                    "(`player_name`,`oldTime`,`guild_name`,`onlineTime`)" +
                    "VALUES (?,?,?,?)"
    ),
    LISTS_GUILD(
            "SELECT * FROM `guild_lists`"
    ),
    //"SELECT * FROM `guild_list` WHERE `guild_name` = ?"
    LIST_GUILD(
            "SELECT * FROM `guild_list` WHERE `guild_name` = ? ORDER BY field(identity,'admin','grand','elite','default')"
    ),
    LIST_APPLY_GUILD(
            "SELECT * FROM `apply` WHERE `guild_name` = ?"
    ),
    LIST_APPLY_NAME(
            "SELECT * FROM `apply` WHERE `player_name` = ?"
    ),
    UPDATE_GUILD_ADMIN(
            "UPDATE guild_lists SET admin_name = ? WHERE `guild_name` = ?"
    ),
    UPDATE_PVP(
            "UPDATE guild_lists SET flag_pvp = ? WHERE `guild_name` = ?"
    ),
    UPDATE_LEVEL(
            "UPDATE guild_lists SET guild_level = ? , max_limit = ? WHERE `guild_name` = ?"
    ),
    UPDATE_TP(
            "UPDATE guild_lists SET tp = ?  WHERE `guild_name` = ?"
    ),
    UPDATE_INFO(
            "UPDATE guild_lists SET guild_info = ?  WHERE `guild_name` = ?"
    ),
    UPDATE_IDENTITY(
            "UPDATE guild_list SET identity = ? WHERE `player_name` = ?"
    ),
    UPDATE_TIME(
            "UPDATE sign SET oldTime = ? , count = ? , onlineTime = ? WHERE `player_name` = ?"
    ),
    DELETE_GUILD(
            "DELETE FROM `guild_lists` WHERE `guild_name` = ?"
    ),
    DELETE_GUILD_ALL_PLAYER(
            "DELETE FROM `guild_list` WHERE `guild_name` = ?"
    ),
    DELETE_GUILD_ALL_APPLY(
            "DELETE FROM `apply` WHERE `guild_name` = ?"
    ),
    DELETE_PLAYER(
            "DELETE FROM `guild_list` WHERE `player_name` = ?"
    ),
    DELETE_APPLY(
            "DELETE FROM `apply` WHERE `player_name` = ?"
    ),
    DELETE_SIGN(
            "DELETE FROM `sign` WHERE `player_name` = ?"
    ),
    DELETE_ALL_SIGN(
            "DELETE FROM `sign` WHERE `guild_name` = ?"
    ),
    FIND_NAME(
            "SELECT * FROM `guild_list` WHERE `player_name` = ?"
    ),
    FIND_GUILD(
            "SELECT * FROM `guild_lists` WHERE `guild_name` = ?"
    ),
    FIND_SIGN(
            "SELECT * FROM `sign` WHERE `player_name` = ?"
    );
    /*
     * 这里可以添加更多的MySQL命令，格式如下
     * COMMAND_NAME(
     *    "YOUR_COMMAND_HERE" +
     *    "YOUR_COMMAND_HERE"
     * );
     */

    private String command;

    SQLCommand(String command)
    {
        this.command = command;
    }
    public String commandToString()
    {
        return command;
    }
}