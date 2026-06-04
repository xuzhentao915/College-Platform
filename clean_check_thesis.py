from docx import Document
import re

def clean_and_check(file_path):
    doc = Document(file_path)
    issues = []
    
    # 计数器
    multiple_spaces_count = 0
    leading_spaces_count = 0
    empty_paras_count = 0
    heading_indent_issues = 0
    
    # 用于检测连续空行
    last_para_was_empty = False

    for i, para in enumerate(doc.paragraphs):
        text = para.text
        style_name = para.style.name

        # 1. 检查空白段落
        if not text.strip():
            if last_para_was_empty:
                empty_paras_count += 1
            last_para_was_empty = True
        else:
            last_para_was_empty = False

        # 2. 检查段首/段尾多余空格
        if text.startswith(' ') or text.startswith('　'):
            leading_spaces_count += 1
        
        # 3. 检查连续多个空格
        if '  ' in text:
            multiple_spaces_count += 1

        # 4. 检查标题缩进 (规则：一二级标题不应缩进)
        if style_name in ['Heading 1', 'Heading 2']:
            if para.paragraph_format.first_line_indent and para.paragraph_format.first_line_indent.pt > 0:
                heading_indent_issues += 1
        
        # 5. 检查残留 Markdown 符号 (如单独的 # 或 ***)
        if re.match(r'^#+\s', text) or text.strip() == '---' or text.strip() == '***':
            issues.append(f"第 {i+1} 段可能残留 Markdown 符号: '{text.strip()}'")

    # 汇总报告
    report = [f"--- 论文深度体检报告 ({file_path}) ---"]
    if empty_paras_count > 0:
        report.append(f"❌ 发现 {empty_paras_count} 处多余的连续空行 (建议删除)。")
    if leading_spaces_count > 0: report.append(f"⚠️ 发现 {leading_spaces_count} 处段首手动空格 (Word 样式已带缩进，请删除手动空格)。")
    if multiple_spaces_count > 0: report.append(f"⚠️ 发现 {multiple_spaces_count} 处文本中存在连续双空格。")
    if heading_indent_issues > 0: report.append(f"❌ 发现 {heading_indent_issues} 处一/二级标题存在错误缩进 (应为0缩进)。")
    
    if not issues and empty_paras_count == 0 and leading_spaces_count == 0:
        report.append("✅ 未发现明显冗余符号或严重格式瑕疵。")
    else:
        report.extend(issues)

    return "\n".join(report)

if __name__ == "__main__":
    import os
    if os.path.exists("毕业论文_已格式化.docx"):
        print(clean_and_check("毕业论文_已格式化.docx"))
    else:
        print("文件不存在。")
