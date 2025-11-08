public interface PaymentService {
    Payment processPayment(int memberId, double amount, String paymentType);
    List<Payment> getMemberPaymentHistory(int memberId);
    boolean hasUnpaidFees(int memberId);
    double calculateBalance(int memberId);
}