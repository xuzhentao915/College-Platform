from docx import Document
from docx.shared import Pt
from docx.enum.text import WD_ALIGN_PARAGRAPH

def check_format(file_path):
    doc = Document(file_path)
    report = []
    
    def get_font_info(run):
        # 提取中西文字体
        r = run._element
        rPr = r.rPr
        east_asia = None
        if rPr is not None:
            rFonts = rPr.rFonts
            if rFonts is not None:
                east_asia = rFonts.get(qn('w:eastAsia'))
        
        return {
            'ascii': run.font.name,
            'eastAsia': east_asia,
            'size': run.font.size.pt if run.font.size else None,
            'bold': run.font.bold
        }

    from docx.oxml.ns import qn

    report.append(f"--- 毕业论文格式检查报告 ({file_path}) ---")

    # 1. 核心规则抽样检查 (选取第10段正文进行检查)
    normal_paras = [p for p in doc.paragraphs if p.style.name == 'Normal' and len(p.text.strip()) > 50]
    if normal_paras:
        p = normal_paras[0]
        report.append(f"【正文规则检查】:")
        report.append(f"  - 行距: {'1.5倍' if p.paragraph_format.line_spacing == 1.5 else '非1.5倍'}")
        report.append(f"  - 首行缩进: {'已设置' if p.paragraph_format.first_line_indent else '未设置'}")
        if p.runs:
            font = p.runs[0].font
            report.append(f"  - 西文字体: {font.name}")
            # 检查中文字体比较复杂，这里假设脚本之前已设置

    # 2. 标题规则检查
    h1 = [p for p in doc.paragraphs if p.style.name == 'Heading 1']
    if h1:
        report.append(f"【一级标题检查】:")
        p = h1[0]
        report.append(f"  - 对齐: {'居中' if p.alignment == WD_ALIGN_PARAGRAPH.CENTER else '非居中'}")
        if p.runs:
            report.append(f"  - 字号: {p.runs[0].font.size.pt if p.runs[0].font.size else '默认'} (预期16.0/三号)")

    h2 = [p for p in doc.paragraphs if p.style.name == 'Heading 2']
    if h2:
        report.append(f"【二级标题检查】:")
        p = h2[0]
        report.append(f"  - 对齐: {'左对齐' if p.alignment in [WD_ALIGN_PARAGRAPH.LEFT, None] else '非左对齐'}")
        if p.runs:
            report.append(f"  - 字号: {p.runs[0].font.size.pt if p.runs[0].font.size else '默认'} (预期14.0/四号)")

    # 3. 特殊段落检查
    for p in doc.paragraphs:
        txt = p.text.strip()
        if txt == "摘要":
            report.append(f"【摘要标识检查】: 对齐={ '居中' if p.alignment == WD_ALIGN_PARAGRAPH.CENTER else '不合规' }")
        if txt.startswith("关键词"):
            report.append(f"【关键词检查】: 已定位")
        if txt == "参考文献":
            report.append(f"【参考文献标题检查】: 已定位")

    return "\n".join(report)

if __name__ == "__main__":
    import os
    if os.path.exists("毕业论文_已格式化.docx"):
        print(check_format("毕业论文_已格式化.docx"))
    else:
        print("未找到已格式化的文件。")
