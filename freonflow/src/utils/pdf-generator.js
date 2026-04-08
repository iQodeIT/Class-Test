import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

export const generatePDF = async (elementId, fileName) => {
  const element = document.getElementById(elementId);
  if (!element) return;

  // Hide action buttons and navbar before capture
  const buttons = element.querySelectorAll('button');
  const navbar = element.querySelector('nav');
  buttons.forEach(btn => btn.style.display = 'none');
  if (navbar) navbar.style.display = 'none';

  try {
    const canvas = await html2canvas(element, {
      scale: 2,
      useCORS: true,
      logging: false,
    });

    const imgData = canvas.toDataURL('image/png');
    const pdf = new jsPDF({
      orientation: 'portrait',
      unit: 'px',
      format: [canvas.width, canvas.height]
    });

    pdf.addImage(imgData, 'PNG', 0, 0, canvas.width, canvas.height);
    pdf.save(`${fileName}.pdf`);
  } catch (error) {
    console.error("PDF Generation failed:", error);
  } finally {
    // Restore elements
    buttons.forEach(btn => btn.style.display = '');
    if (navbar) navbar.style.display = '';
  }
};
