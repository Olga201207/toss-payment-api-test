const CLIENT_KEY = 'test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq';
const tossPayments = TossPayments(CLIENT_KEY);

document.getElementById('order-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const formData = new FormData(e.target);
    const orderData = {
        orderName: formData.get('orderName'),
        amount: parseFloat(formData.get('amount')),
        customerName: formData.get('customerName'),
        customerEmail: formData.get('customerEmail')
    };

    try {
        // 주문 생성
        const orderResponse = await fetch('/api/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(orderData)
        });

        const orderResult = await orderResponse.json();

        if (!orderResult.success) {
            alert('주문 생성 실패: ' + orderResult.message);
            return;
        }

        // 토스페이먼츠 결제 요청
        tossPayments.requestPayment('카드', {
            amount: orderData.amount,
            orderId: orderResult.data.orderId,
            orderName: orderData.orderName,
            customerName: orderData.customerName,
            successUrl: `${window.location.origin}/success.html`,
            failUrl: `${window.location.origin}/fail.html`,
        });

    } catch (error) {
        console.error('주문 생성 중 오류:', error);
        alert('주문 생성 중 오류가 발생했습니다.');
    }
});