'use client';

import { useEffect, useState } from 'react';

export default function AdminPage() {
    const [quotations, setQuotations] = useState([]);
    const [file, setFile] = useState<File | null>(null);
    const [selectedId, setSelectedId] = useState('');

    useEffect(() => {
        fetch('http://localhost:8080/api/quotations')
            .then(res => res.json())
            .then(data => setQuotations(data))
            .catch(err => console.error(err));
    }, []);

    const handleUpload = async () => {
        if (!selectedId) {
            alert("Lütfen bir teklif seçiniz.");
            return;
        }

        if (!file) {
            alert("Lütfen yüklenecek dosyayı seçiniz.");
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('quotationId', selectedId);

        try {
            const res = await fetch(`http://localhost:8080/api/quotations/import`, {
                method: 'POST',
                body: formData,
            });

            if (res.ok) {
                alert("İşlem başarıyla tamamlandı.");
                window.location.reload();
            } else {
                alert("Dosya içeriği hatalı veya fiyatlar girilmemiş.");
            }
        } catch (error) {
            alert("Sunucuya bağlanılamadı.");
        }
    };

    return (
        <main className="min-h-screen bg-[#0f1115] text-white p-10 font-sans text-sm">
            <h1 className="text-xl font-bold mb-10 text-center uppercase tracking-widest">
                Teklif Yönetimi
            </h1>

            <div className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-8">

                <div className="lg:col-span-2 space-y-2">
                    <p className="text-[10px] text-gray-500 uppercase tracking-widest mb-4 ml-1">Bekleyen Talepler</p>
                    {quotations.map((q: any) => (
                        <div
                            key={q.id}
                            onClick={() => setSelectedId(String(q.id))}
                            className={`p-4 rounded-lg border transition-all cursor-pointer flex justify-between items-center ${selectedId === String(q.id) ? 'border-blue-500 bg-blue-500/5' : 'border-gray-800 bg-[#1a1d23] hover:border-gray-700'
                                }`}
                        >
                            <div className="font-medium text-gray-300">{q.customerEmail}</div>
                            <span className={`text-[10px] font-bold px-2 py-1 rounded border ${q.status === 'PRICED'
                                    ? 'text-green-500 border-green-500/20 bg-green-500/5'
                                    : 'text-amber-500 border-amber-500/20 bg-amber-500/5'
                                }`}>
                                {q.status === 'PRICED' ? 'TAMAMLANDI' : 'BEKLİYOR'}
                            </span>
                        </div>
                    ))}
                </div>

                <div className="bg-[#1a1d23] p-8 rounded-xl border border-gray-800 h-fit sticky top-10">
                    <div className="text-center mb-8">
                        <span className="text-[10px] text-gray-600 uppercase tracking-widest block mb-2">Seçili ID</span>
                        <p className="text-3xl font-bold text-blue-500">#{selectedId || "---"}</p>
                    </div>

                    <div className="space-y-6">
                        <input
                            type="file"
                            onChange={(e) => setFile(e.target.files?.[0] || null)}
                            className="block w-full text-xs text-gray-500 file:mr-3 file:py-2 file:px-4 file:rounded file:border-0 file:bg-gray-800 file:text-white cursor-pointer"
                        />
                        <button
                            onClick={handleUpload}
                            className="w-full py-4 bg-blue-600 hover:bg-blue-500 text-white rounded font-bold text-[11px] transition-all uppercase tracking-widest"
                        >
                            GÖNDER
                        </button>
                    </div>
                </div>

            </div>
        </main>
    );
}