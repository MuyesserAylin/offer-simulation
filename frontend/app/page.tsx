'use client';

import { useEffect, useState } from 'react';
import { getProducts } from '@/services/api';

export default function Home() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState<any[]>([]); // Sepetimiz
  const [email, setEmail] = useState('');

  // Sayfa yüklendiğinde veritabanından ürünleri çekiyoruz
  useEffect(() => {
    getProducts().then(data => {
      setProducts(data);
    });
  }, []);

  // Sepete ekle/çıkar mantığı
  const toggleCart = (product: any) => {
    const isAlreadyInCart = cart.find(item => item.id === product.id);
    if (isAlreadyInCart) {
      setCart(cart.filter(item => item.id !== product.id));
    } else {
      setCart([...cart, product]);
    }
  };

  const handleDownloadExcel = () => {
    // 1. Kontroller
    if (!email) {
      alert("Mailinizi girmediniz!");
      return;
    }
    if (cart.length === 0) {
      alert("Sepet boş, teklif alacak ürün seçmediniz!");
      return;
    }


    const productIds = cart.map(item => item.id).join(',');


    const downloadUrl = `http://localhost:8080/api/quotations/export-products?email=${encodeURIComponent(email)}&productIds=${productIds}`;


    window.location.href = downloadUrl;
  };

  return (
    <main className="flex min-h-screen flex-col items-center p-12 bg-black text-white font-sans">
      <h1 className="text-3xl font-extrabold mb-2 text-blue-500">OFFER SIMULATION</h1>
      <p className="text-gray-400 mb-10">Müşteri için teklif sepetini hazırla</p>

      {/* Üst Kısım: Mail Girişi */}
      <div className="w-full max-w-2xl mb-8 p-6 bg-gray-900 rounded-2xl border border-gray-800 shadow-xl">
        <label className="block mb-3 text-sm font-medium text-gray-300">Müşteri E-Posta Adresi</label>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="ornegin@sirket.com"
          className="w-full p-3 rounded-xl bg-black border border-gray-700 focus:border-blue-500 focus:ring-1 focus:ring-blue-500 outline-none transition-all"
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-10 w-full max-w-6xl">

        {/* SOL: Ürün Kataloğu */}
        <div className="bg-gray-900 p-6 rounded-2xl border border-gray-800 shadow-lg">
          <h2 className="text-xl font-bold mb-6 flex items-center gap-2">
            📦 Ürün Kataloğu
          </h2>
          <div className="space-y-3 max-h-[400px] overflow-y-auto pr-2 custom-scrollbar">
            {products.map((p: any) => (
              <div key={p.id} className="flex justify-between items-center p-4 bg-black/50 rounded-xl border border-gray-800 hover:border-gray-600 transition-colors">
                <div>
                  <p className="font-semibold text-gray-200">{p.name}</p>
                  <p className="text-xs text-green-500">{p.last_quoted_price} TL</p>
                </div>
                <button
                  onClick={() => toggleCart(p)}
                  className={`px-4 py-2 rounded-lg text-xs font-bold transition-all ${cart.find(item => item.id === p.id)
                    ? 'bg-red-900/30 text-red-500 border border-red-900/50 hover:bg-red-900/50'
                    : 'bg-blue-600 text-white hover:bg-blue-500 shadow-lg shadow-blue-900/20'
                    }`}
                >
                  {cart.find(item => item.id === p.id) ? 'Sepetten Çıkar' : 'Sepete Ekle'}
                </button>
              </div>
            ))}
          </div>
        </div>

        {/* SAĞ: Sepet Özeti ve İşlem */}
        <div className="bg-gray-900 p-6 rounded-2xl border border-blue-900/20 shadow-lg flex flex-col">
          <h2 className="text-xl font-bold mb-6 flex items-center gap-2">
            🛒 Teklif Sepeti ({cart.length})
          </h2>

          <div className="flex-grow">
            {cart.length === 0 ? (
              <div className="text-center py-10 text-gray-500 italic border-2 border-dashed border-gray-800 rounded-xl">
                Sepet boş, ürün ekle...
              </div>
            ) : (
              <ul className="space-y-2">
                {cart.map(item => (
                  <li key={item.id} className="flex items-center gap-2 text-sm text-gray-300 bg-black/30 p-2 rounded-lg">
                    <span className="text-blue-500">✔</span> {item.name}
                  </li>
                ))}
              </ul>
            )}
          </div>

          <button
            onClick={handleDownloadExcel}
            className="mt-8 w-full py-4 bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-500 hover:to-blue-600 text-white rounded-xl font-bold text-lg shadow-2xl shadow-blue-900/40 transition-all transform active:scale-95"
          >
            Teklif Dosyasını Hazırla (XLSX)
          </button>
        </div>

      </div>
    </main>
  );
}