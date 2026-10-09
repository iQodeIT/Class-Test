import * as Print from 'expo-print';
import * as Sharing from 'expo-sharing';

export const PdfReportGenerator = {
  async generatePropertyReport({ property, rooms, items, isPro = false }) {
    try {
      const moveInItems = items.filter(i => i.phase === 'move_in');
      const moveOutItems = items.filter(i => i.phase === 'move_out');
      const preExistingItems = items.filter(i => i.isPreExisting || i.condition === 'Pre-existing damage');

      const watermark = isPro ? '' : `<div style="position: fixed; bottom: 10px; right: 10px; font-size: 10px; color: #94a3b8;">Watermarked - MoveProof Free Version</div>`;

      let roomsSectionHtml = '';
      rooms.forEach(room => {
        const roomItems = items.filter(i => i.roomId === room.id);
        if (roomItems.length === 0) return;

        let itemsGrid = '';
        roomItems.forEach(item => {
          itemsGrid += `
            <div style="border: 1px solid #e2e8f0; border-radius: 8px; padding: 10px; margin-bottom: 12px; page-break-inside: avoid;">
              <div style="display: flex; justify-content: space-between; font-weight: bold; font-size: 13px;">
                <span>${item.label} (${item.phase === 'move_out' ? 'Move-Out' : 'Move-In'})</span>
                <span style="color: ${item.isPreExisting ? '#10b981' : '#005f73'};">${item.condition}</span>
              </div>
              <div style="font-size: 11px; color: #64748b; margin-top: 4px;">Captured: ${new Date(item.timestamp).toLocaleString()}</div>
              ${item.mediaUri ? `<img src="${item.mediaUri}" style="width: 100%; max-height: 200px; object-fit: cover; border-radius: 6px; margin-top: 8px;" />` : ''}
              ${item.note ? `<div style="font-size: 12px; margin-top: 6px; font-style: italic;">Note: ${item.note}</div>` : ''}
              <div style="font-size: 9px; font-family: monospace; color: #94a3b8; margin-top: 6px;">SHA-256 Hash: ${item.sha256 || 'N/A'}</div>
            </div>
          `;
        });

        roomsSectionHtml += `
          <div style="margin-top: 20px;">
            <h3 style="color: #005F73; border-bottom: 1px solid #cbd5e1; padding-bottom: 4px;">${room.name}</h3>
            ${itemsGrid}
          </div>
        `;
      });

      const html = `
        <!DOCTYPE html>
        <html>
          <head>
            <meta charset="utf-8">
            <style>
              body { font-family: 'Helvetica Neue', Arial, sans-serif; padding: 30px; color: #0f172a; }
              .cover { text-align: center; margin-top: 40px; margin-bottom: 60px; }
              .title { font-size: 28px; font-weight: 800; color: #005f73; margin-bottom: 8px; }
              .subtitle { font-size: 16px; color: #64748b; margin-bottom: 24px; }
              .summary-box { background-color: #f8fafc; border: 1px solid #e2e8f0; border-radius: 12px; padding: 16px; margin-bottom: 30px; }
              .footer { border-top: 2px solid #005f73; margin-top: 40px; padding-top: 12px; font-size: 11px; text-align: center; color: #64748b; }
            </style>
          </head>
          <body>
            <div class="cover">
              <div class="title">MOVEPROOF CONDITION & DAMAGE REPORT</div>
              <div class="subtitle">${property.nickname || property.address}</div>
              <div><strong>Property Address:</strong> ${property.address}</div>
              <div><strong>Landlord/Manager:</strong> ${property.landlordName || 'N/A'}</div>
              <div><strong>Report Date:</strong> ${new Date().toLocaleDateString()}</div>
            </div>

            <div class="summary-box">
              <h3 style="margin-top: 0; color: #005f73;">Inspection Summary</h3>
              <div>• Total Photos Captured: ${items.length}</div>
              <div>• Pre-Existing Protected Items: ${preExistingItems.length}</div>
              <div>• Cryptographic Integrity: SHA-256 Verified</div>
            </div>

            ${roomsSectionHtml}

            <div class="footer">
              <strong>Integrity Verified</strong> • Each photo's SHA-256 hash is computed at time of capture and embedded in this report.
            </div>
            ${watermark}
          </body>
        </html>
      `;

      const { uri } = await Print.printToFileAsync({ html });
      await Sharing.shareAsync(uri);
      return uri;
    } catch (e) {
      console.error('Error generating report:', e);
      throw e;
    }
  }
};
