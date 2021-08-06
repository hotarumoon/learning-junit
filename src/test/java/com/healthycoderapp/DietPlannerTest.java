package com.healthycoderapp;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DietPlannerTest {

    private DietPlanner dietPlanner;

    @BeforeEach
    void setup(){
        this.dietPlanner = new DietPlanner(20,30,50);
    }

    @AfterEach
    void afterEach(){
        System.out.println("A unit test was finished");
    }


    @Nested
    class GetBMIScoresTests{
        //When you are using random numbers, u may want to repeat it
        @RepeatedTest(10)
        void should_ReturnCorrectDietPlan_When_CorrectCoder(){
            //Given
            Coder coder = new Coder(1.63, 74.0, 29, Gender.FEMALE);
            DietPlan expected = new DietPlan(1847, 92, 62, 231);
            //When
            DietPlan actual = dietPlanner.calculateDiet(coder);
            //System.out.printf(actual.toString());
            //Then
            assertAll(
                    ()-> assertEquals(expected.getCalories(), actual.getCalories()),
                    ()-> assertEquals(expected.getCarbohydrate(), actual.getCarbohydrate()),
                    ()-> assertEquals(expected.getFat(), actual.getFat())

            );
        }
    }


}
