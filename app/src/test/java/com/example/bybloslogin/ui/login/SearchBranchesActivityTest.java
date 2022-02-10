package com.example.bybloslogin.ui.login;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class SearchBranchesActivityTest {

    @Test
    public void testBranchContainsHours() {
        Branch testBranch1 = new Branch("TestBranch", "BranchAddress", "9:00\n17:0\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00", new ArrayList<String>());
        boolean result1 = SearchBranchesActivity.branchContainsHour(testBranch1, "9:00AM");
        assertEquals(result1, true);
    }

//    @Test
//    public void testBranchContainsHours() {
//        Branch testBranch1 = new Branch("TestBranch", "BranchAddress", "9:00\n17:0\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00", new ArrayList<String>());
//        boolean result1 = SearchBranchesActivity.branchContainsHour(testBranch1, "9:00AM");
//        assertThat(result1, is(true));
//    }
//
//    @Test
//    public void testBranchContainsHours() {
//        Branch testBranch1 = new Branch("TestBranch", "BranchAddress", "9:00\n17:0\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00", new ArrayList<String>());
//        boolean result1 = SearchBranchesActivity.branchContainsHour(testBranch1, "9:00AM");
//        assertThat(result1, is(true));
//    }
//
//    @Test
//    public void testBranchContainsHours() {
//        Branch testBranch1 = new Branch("TestBranch", "BranchAddress", "9:00\n17:0\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00\n9:00\n17:00", new ArrayList<String>());
//        boolean result1 = SearchBranchesActivity.branchContainsHour(testBranch1, "9:00AM");
//        assertThat(result1, is(true));
//    }

}