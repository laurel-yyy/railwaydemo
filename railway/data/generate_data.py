import re

# 中文城市名到英文的映射
city_mapping = {
    '北京南': 'New York',
    '北京': 'New York',
    '济南西': 'Chicago',
    '南京南': 'San Francisco',
    '南京': 'San Francisco',
    '杭州东': 'Boston',
    '杭州': 'Los Angeles',
    '宁波': 'Philadelphia',
    '德州': 'Chicago',
    '嘉兴': 'Washington D.C.',
    '海宁': 'Denver',
}

# 直接硬编码文件名
INPUT_FILE = "t_seat.sql"
OUTPUT_FILE = "t_seat_converted.sql"
START_LINE = 330

def convert_sql_file(input_file, output_file, start_line):
    try:
        # 读取文件内容，跳过前面的行
        with open(input_file, 'r', encoding='utf-8') as f:
            all_lines = f.readlines()
            
        # 只处理从start_line开始的行
        lines_to_process = all_lines[start_line-1:]
        sql_content = ''.join(lines_to_process)
        
        # 替换城市名称 - 使用更精确的模式匹配
        for chinese, english in city_mapping.items():
            # 使用更明确的模式来匹配城市名称
            sql_content = sql_content.replace(f", '{chinese}',", f", '{english}',")
            sql_content = sql_content.replace(f", '{chinese}', ", f", '{english}', ")
            sql_content = sql_content.replace(f"', '{chinese}',", f"', '{english}',")
            sql_content = sql_content.replace(f"', '{chinese}', ", f"', '{english}', ")
        
        # 将文件写出
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(sql_content)
        
        print(f"转换完成！输出文件: {output_file}")
        print(f"处理了从第{start_line}行开始的内容")
        
    except Exception as e:
        print(f"发生错误: {e}")

if __name__ == "__main__":
    print(f"开始处理文件 {INPUT_FILE}")
    print(f"从第 {START_LINE} 行开始")
    convert_sql_file(INPUT_FILE, OUTPUT_FILE, START_LINE)