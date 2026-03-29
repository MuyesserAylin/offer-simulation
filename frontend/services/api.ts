const BASE_URL = 'http://localhost:8080/api';

export const getProducts = async () => {
    try {
        const response = await fetch(`${BASE_URL}/products`);
        if (!response.ok) {
            throw new Error('Veriler çekilemedi!');
        }
        return await response.json();
    } catch (error) {
        console.error("Ürünler çekilirken hata oluştu:", error);
        return [];
    }
};