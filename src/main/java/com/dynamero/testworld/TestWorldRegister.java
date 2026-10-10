package com.dynamero.testworld;

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