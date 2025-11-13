// src/main/java/com/karateclub/tests/ComprehensiveTestRunner.java
package com.karateclub.tests;

public class ComprehensiveTestRunner {
    public static void main(String[] args) {
        System.out.println("🥋 KARATE CLUB - COMPREHENSIVE TEST SUITE");
        System.out.println("==========================================\n");

        int totalTests = 0;
        int passedTests = 0;
        int failedTests = 0;

        // Test 1: Database Initialization
        System.out.println("🧪 TEST 1: DATABASE INITIALIZATION");
        System.out.println("-----------------------------------");
        try {
            DatabaseInitializer.main(new String[]{});
            System.out.println("✅ DatabaseInitializer - PASSED");
            passedTests++;
        } catch (Exception e) {
            System.out.println("❌ DatabaseInitializer - FAILED: " + e.getMessage());
            e.printStackTrace();
            failedTests++;
        }
        totalTests++;
        System.out.println();

        // Test 2: Hibernate Configuration
        System.out.println("🧪 TEST 2: HIBERNATE CONFIGURATION");
        System.out.println("-----------------------------------");
        try {
            TestHibernate.main(new String[]{});
            System.out.println("✅ TestHibernate - PASSED");
            passedTests++;
        } catch (Exception e) {
            System.out.println("❌ TestHibernate - FAILED: " + e.getMessage());
            failedTests++;
        }
        totalTests++;
        System.out.println();

        // Test 3: Entity Mapping
        System.out.println("🧪 TEST 3: ENTITY MAPPING");
        System.out.println("--------------------------");
        try {
            TestEntities.main(new String[]{});
            System.out.println("✅ TestEntities - PASSED");
            passedTests++;
        } catch (Exception e) {
            System.out.println("❌ TestEntities - FAILED: " + e.getMessage());
            failedTests++;
        }
        totalTests++;
        System.out.println();

        // Test 4: DAO Layer
        System.out.println("🧪 TEST 4: DAO LAYER OPERATIONS");
        System.out.println("-------------------------------");
        try {
            TestDAOLayer.main(new String[]{});
            System.out.println("✅ TestDAOLayer - PASSED");
            passedTests++;
        } catch (Exception e) {
            System.out.println("❌ TestDAOLayer - FAILED: " + e.getMessage());
            failedTests++;
        }
        totalTests++;
        System.out.println();

        // Test 5: Service Layer
        System.out.println("🧪 TEST 5: SERVICE LAYER OPERATIONS");
        System.out.println("-----------------------------------");
        try {
            ServiceLayerTest.main(new String[]{});
            System.out.println("✅ ServiceLayerTest - PASSED");
            passedTests++;
        } catch (Exception e) {
            System.out.println("❌ ServiceLayerTest - FAILED: " + e.getMessage());
            failedTests++;
        }
        totalTests++;
        System.out.println();

        // Test 6: Advanced Integration
        System.out.println("🧪 TEST 6: ADVANCED INTEGRATION TESTS");
        System.out.println("-------------------------------------");
        try {
            AdvancedIntegrationTest.main(new String[]{});
            System.out.println("✅ AdvancedIntegrationTest - PASSED");
            passedTests++;
        } catch (Exception e) {
            System.out.println("❌ AdvancedIntegrationTest - FAILED: " + e.getMessage());
            failedTests++;
        }
        totalTests++;
        System.out.println();

        // Test 7: Authentication System
        System.out.println("🧪 TEST 7: AUTHENTICATION SYSTEM");
        System.out.println("--------------------------------");
        try {
            AuthServiceTest.main(new String[]{});
            System.out.println("✅ AuthServiceTest - PASSED");
            passedTests++;
        } catch (Exception e) {
            System.out.println("❌ AuthServiceTest - FAILED: " + e.getMessage());
            failedTests++;
        }
        totalTests++;
        System.out.println();

        // SUMMARY
        System.out.println("📊 TEST SUMMARY");
        System.out.println("================");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("✅ Passed: " + passedTests);
        System.out.println("❌ Failed: " + failedTests);
        System.out.println("📈 Success Rate: " + (passedTests * 100 / totalTests) + "%");

        if (failedTests == 0) {
            System.out.println("\n🎉 ALL TESTS PASSED! SYSTEM IS PRODUCTION-READY! 🎉");
        } else {
            System.out.println("\n⚠️  " + failedTests + " test(s) failed. Please check the errors above.");
        }
    }
}