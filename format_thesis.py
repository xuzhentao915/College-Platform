import os
from docx import Document
from docx.shared import Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH, WD_LINE_SPACING
from docx.oxml.ns import qn

def format_word(file_path):
    if not os.path.exists(file_path):
        print(f"File {file_path} not found.")
        return

    doc = Document(file_path)

    # 辅助函数：设置中西文字体
    def set_font(run, chinese_font='宋体', western_font='Times New Roman', size=12, bold=False):
        run.font.name = western_font
        run.font.size = Pt(size)
        run.font.bold = bold
        # 设置中文字体
        r = run._element
        rPr = r.get_or_add_rPr()
        rFonts = rPr.get_or_add_rFonts()
        rFonts.set(qn('w:eastAsia'), chinese_font)

    # 全局正文样式设置 (宋体小四, 1.5倍行距)
    style = doc.styles['Normal']
    font = style.font
    font.name = 'Times New Roman'
    font.size = Pt(12) # 小四是12pt
    style.paragraph_format.line_spacing = 1.5
    style.paragraph_format.first_line_indent = Pt(24) # 2字符缩进约24pt

    for para in doc.paragraphs:
        # 处理一级标题 (Heading 1)
        if para.style.name == 'Heading 1':
            para.alignment = WD_ALIGN_PARAGRAPH.CENTER
            for run in para.runs:
                set_font(run, chinese_font='黑体', size=16, bold=True) # 三号是16pt
            para.paragraph_format.line_spacing = 1.5
            para.paragraph_format.first_line_indent = Pt(0) # 标题不缩进

        # 处理二级标题 (Heading 2)
        elif para.style.name == 'Heading 2':
            para.alignment = WD_ALIGN_PARAGRAPH.LEFT
            for run in para.runs:
                set_font(run, chinese_font='黑体', size=14) # 四号是14pt
            para.paragraph_format.line_spacing = 1.5
            para.paragraph_format.first_line_indent = Pt(0)

        # 处理三级标题 (Heading 3)
        elif para.style.name == 'Heading 3':
            para.alignment = WD_ALIGN_PARAGRAPH.LEFT
            para.paragraph_format.first_line_indent = Pt(24) # 左缩进2字符
            for run in para.runs:
                set_font(run, chinese_font='黑体', size=12) # 小四是12pt
            para.paragraph_format.line_spacing = 1.5

        # 处理正文内容
        else:
            # 排除摘要、关键词等特殊段落 (简单根据文本内容判断)
            text = para.text.strip()
            if text == "摘要":
                para.alignment = WD_ALIGN_PARAGRAPH.CENTER
                for run in para.runs:
                    set_font(run, chinese_font='黑体', size=16)
                continue
            
            if text.startswith("关键词"):
                para.paragraph_format.first_line_indent = Pt(0)
                # 分段处理：关键词三个字用黑体
                # 注意：docx 转换出来的 runs 可能没这么精细，这里只能大致设置
                for run in para.runs:
                    set_font(run, chinese_font='宋体', size=12)
                continue

            # 普通正文
            para.paragraph_format.line_spacing = 1.5
            para.paragraph_format.first_line_indent = Pt(24)
            for run in para.runs:
                set_font(run, chinese_font='宋体', size=12)

    # 保存结果
    output_path = "毕业论文_已格式化.docx"
    doc.save(output_path)
    print(f"Formatted document saved to {output_path}")

if __name__ == "__main__":
    format_word("毕业论文.docx")
