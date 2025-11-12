package com.karateclub.TestFeatures;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private static final List<TestResult> testResults = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("🚀 STARTING COMPREHENSIVE TEST SUITE");
        System.out.println("=====================================\n");

        boolean allTestsPassed = true;

        try {
            // Phase 1: Database Setup
            System.out.println("📊 PHASE 1: DATABASE SETUP");
            System.out.println("--------------------------");
            allTestsPassed &= runTest("Database Initialization", () -> {
                DatabaseInitializer.initializeSampleData();
                System.out.println("✅ Database populated with sample data");
            });

            // Phase 2: Hibernate & Entity Tests
            System.out.println("\n🏗️  PHASE 2: HIBERNATE & ENTITY TESTS");
            System.out.println("-----------------------------------");
            allTestsPassed &= runTest("Hibernate Configuration", () -> {
                TestHibernate.main(new String[]{});
                System.out.println("✅ Hibernate configuration verified");
            });

            allTestsPassed &= runTest("Entity Mapping", () -> {
                TestEntities.main(new String[]{});
                System.out.println("✅ Entity mappings verified");
            });

            // Phase 3: DAO Layer Tests
            System.out.println("\n💾 PHASE 3: DAO LAYER TESTS");
            System.out.println("---------------------------");
            allTestsPassed &= runTest("DAO Operations", () -> {
                TestDAOLayer.main(new String[]{});
                System.out.println("✅ DAO layer operations verified");
            });

            // Phase 4: Service Layer Tests
            System.out.println("\n⚙️  PHASE 4: SERVICE LAYER TESTS");
            System.out.println("-------------------------------");
            allTestsPassed &= runTest("Service Layer", () -> {
                ServiceLayerTest.main(new String[]{});
                System.out.println("✅ Service layer operations verified");
            });

            // Phase 5: Integration Tests
            System.out.println("\n🔗 PHASE 5: INTEGRATION TESTS");
            System.out.println("-----------------------------");
            allTestsPassed &= runTest("Advanced Integration", () -> {
                AdvancedIntegrationTest.main(new String[]{});
                System.out.println("✅ Advanced integration tests passed");
            });

        } catch (Exception e) {
            System.out.println("❌ Test suite interrupted: " + e.getMessage());
            allTestsPassed = false;
        }

        // Final Results
        printTestSummary(allTestsPassed);
    }

    private static boolean runTest(String testName, Runnable test) {
        System.out.println("🧪 Running: " + testName);

        try {
            test.run();
            testResults.add(new TestResult(testName, true, "Success"));
            return true;
        } catch (Exception e) {
            System.out.println("❌ " + testName + " FAILED: " + e.getMessage());
            testResults.add(new TestResult(testName, false, e.getMessage()));
            return false;
        }
    }

    private static void printTestSummary(boolean allTestsPassed) {
        System.out.println("\n📈 TEST SUMMARY");
        System.out.println("===============");

        int passed = 0, failed = 0;
        for (TestResult result : testResults) {
            if (result.passed) {
                System.out.println("✅ " + result.testName);
                passed++;
            } else {
                System.out.println("❌ " + result.testName + " - " + result.errorMessage);
                failed++;
            }
        }

        System.out.println("\n📊 FINAL RESULTS:");
        System.out.println("   Total Tests: " + testResults.size());
        System.out.println("   ✅ Passed: " + passed);
        System.out.println("   ❌ Failed: " + failed);
        System.out.println("   📈 Success Rate: " + (passed * 100 / testResults.size()) + "%");

        if (allTestsPassed) {
            System.out.println("\n🎉 ALL TESTS PASSED! SYSTEM IS PRODUCTION-READY! 🎉");
        } else {
            System.out.println("\n⚠️  SOME TESTS FAILED. PLEASE REVIEW THE ERRORS ABOVE.");
        }
    }

    private static class TestResult {
        String testName;
        boolean passed;
        String errorMessage;

        TestResult(String testName, boolean passed, String errorMessage) {
            this.testName = testName;
            this.passed = passed;
            this.errorMessage = errorMessage;
        }
    }
}