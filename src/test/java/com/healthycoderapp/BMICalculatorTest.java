package com.healthycoderapp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public class BMICalculatorTest {

    private String environment = "dev";
    //This method must be static
    //Used ofr Setting up db connections start servers etc
    @BeforeAll
    static void beforeAll(){
        System.out.println("Before All Unit Tests");
    }

    //Close db connections stop servers etc
    @AfterAll
    static void afterAll(){
        System.out.println("After all unit tests");
    }

    //Nested hepls you group the tests with same purpose

    //You can have inner classes in inner classes , you define the hierarchy

    //Each innerclass can have their own before each after eact etc methods
    @Nested
    @DisplayName(">>>>INNER CLASS DISPLAY NAME HELLOOOO")
    class IsDietRecommendedTests{
        @ParameterizedTest(name = "weight={0}, height={1}")
        //@ValueSource(doubles = {80.0, 89.0, 95.0, 110.0})
        //@CsvSource(value = {"89.0, 1.72", "95.0, 1.75"})
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void should_Return_True_When_DietRecommended(Double coderWeight, Double coderHeight) {

            //given
            double weight = coderWeight;
            double height = coderHeight;
            //when
            boolean recommended = BMICalculator.isDietRecommended(weight,height);
            //then
            assertTrue(recommended);
        }


        @Test
        @DisplayName(">>>>DISPLAY NAME HELLOOOO")
        //Skips the test and does not execute it
        @Disabled
        //@DisabledOnOs(OS.MAC) --> Skips test if on MAC os
        void should_Return_False_When_DietNotRecommended() {

            //given
            double weight = 50.0;
            double height = 1.92;
            //when
            boolean recommended = BMICalculator.isDietRecommended(weight,height);
            //then
            assertFalse(recommended);
        }

        @Test
        void should_Return_ArithmeticException_When_HeightZero() {

            //given
            double weight = 50.0;
            double height = 0.0;
            //when
            Executable executable = () -> BMICalculator.isDietRecommended(weight,height);
            //then

            //If it throws an exception, the code below would never work
            //In order to test this, do not execute the method, create an executable instead and give that to the methos below
            assertThrows(ArithmeticException.class,executable );
        }

    }


    @Nested
    class FindCoderWithWorstBMITests{
        @Test
        void should_ReturnCoderWithWorstBMI_When_CoderListNotEmpty(){
            //Given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.63, 74.0));
            coders.add(new Coder(1.82, 198.0));
            //When
            Coder coderWorstBmi = BMICalculator.findCoderWithWorstBMI(coders);
            //Then

            //IF you want to test all of the above use assert all
            assertAll(
                    ()-> assertEquals(1.82, coderWorstBmi.getHeight()),
                    () ->  assertEquals(198.0, coderWorstBmi.getWeight())
            );
        }

        @Test
        void should_ReturnNullWorstBMICoder_When_CoderListEmpty(){
            //Given
            List<Coder> coders = new ArrayList<>();
            //When
            Coder coderWorstBmi = BMICalculator.findCoderWithWorstBMI(coders);
            //Then
            assertNull(coderWorstBmi);
        }


        @Test
        void should_ReturnEmptyWorstBMICoder_When_CoderListNotEmpty(){

            //This test will only run when the env is prod
            //If not this test will be skipped
            assumeTrue(BMICalculatorTest.this.environment.equals("prod"));
            //Given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82,98.0));
            coders.add(new Coder(1.82, 64.7));
            double[] expected = {18.52, 29.59, 19.53};
            //When
            double[] bmiScores = BMICalculator.getBMIScores(coders);

            //Then
            //AssertEquals checks if they are the same object in the memory
            //Eventhough the elements are the same, the test will fail
            //In order to properly test array equality, use assertArrayEquals
            //assertEquals(expected,bmiScores);
            assertArrayEquals(expected,bmiScores);
        }

        @Test
        void should_ReturnCoderWithWorstBMIIn1Ms_Wheb_CoderListHas10000Elements(){
            //Given
            List<Coder> coders = new ArrayList<>();
            for(int i=0; i< 10000; i++){
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }
            //When
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            //Then
            assertTimeout(Duration.ofMillis(500), executable);
        }

    }


}
