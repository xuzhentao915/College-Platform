from docx import Document
from docx.shared import Pt
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml.ns import qn
import re

def fix_and_check(input_path, output_path):
    doc = Document(input_path)
    
    # 辅助函数：设置中西文字体
    def set_font(run, chinese_font='宋体', western_font='Times New Roman', size=12, bold=False):
        run.font.name = western_font
        run.font.size = Pt(size)
        run.font.bold = bold
        r = run._element
        rPr = r.get_or_add_rPr()
        rFonts = rPr.get_or_add_rFonts()
        rFonts.set(qn('w:eastAsia'), chinese_font)

    # 1. 全局清理与修复
    paragraphs = list(doc.paragraphs)
    to_remove = []
    last_was_empty = False

    for i, para in enumerate(paragraphs):
        # A. 修复连续空格：将两个及以上空格替换为一个
        if '  ' in para.text:
            para.text = re.sub(r' +', ' ', para.text)

        # B. 标记连续空行以便删除
        if not para.text.strip():
            if last_was_empty:
                to_remove.append(para)
            last_was_empty = True
        else:
            last_was_empty = False

        # C. 专项修复：摘要居中
        if para.text.strip() == "摘要":
            para.alignment = WD_ALIGN_PARAGRAPH.CENTER
            for run in para.runs:
                set_font(run, chinese_font='黑体', size=16, bold=True)

        # D. 标题样式深度加固 (确保0缩进)
        if para.style.name in ['Heading 1', 'Heading 2']:
            para.paragraph_format.first_line_indent = Pt(0)
            para.paragraph_format.left_indent = Pt(0)
            if para.style.name == 'Heading 1':
                para.alignment = WD_ALIGN_PARAGRAPH.CENTER
            else:
                para.alignment = WD_ALIGN_PARAGRAPH.LEFT

        # E. 正文缩进加固 (2字符缩进)
        if para.style.name == 'Normal' and para.text.strip() and para.text.strip() != "摘要" and not para.text.startswith("关键词"):
            para.paragraph_format.first_line_indent = Pt(24) # 约2字符
            para.paragraph_format.line_spacing = 1.5

    # 执行物理删除空行
    for para in to_remove:
        p = para._element
        p.getparent().remove(p)

    doc.save(output_path)
    return f"✅ 修复完成！最终定稿已保存至: {output_path}"

if __name__ == "__main__":
    import os
    input_file = "毕业论文_已格式化.docx"
    output_file = "毕业论文_最终定稿.docx"
    if os.path.exists(input_file):
        print(fix_and_check(input_file, output_file))
    else:
        print("未找到输入文件。")
