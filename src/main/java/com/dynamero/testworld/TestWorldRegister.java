package com.dynamero.testworld;

import com.dynamero.shared.annotations.*;

@Developer("TurtyWurty")
@CreatedAt("2026-10-10")
@ModifiedAt("2026-10-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class TestWorldRegister
{
    public static TestWorldGemerator EMPTY = new BaseTestWorld();

    public static void init(TestWorldGemerator generator)
    {
        if(generator != null)
        {
            TestWorld.init(generator);
            return;
        }

        TestWorld.init(EMPTY);
    }

    public static class BaseTestWorld implements TestWorldGemerator
    {
        @Override
        public void generate(TestWorldContext context)
        {

        }
    }
}