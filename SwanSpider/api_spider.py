# coding:utf-8

import bs4
import requests
import HTMLParser
import json

parser = HTMLParser.HTMLParser()
# 遍历控件列表
content = requests.get("https://smartprogram.baidu.com/docs/develop/api/apilist/").content
soup = bs4.BeautifulSoup(content, "lxml")
api_tags = soup.select('table td>a')
output = []
for component in api_tags:
    api_json = {}
    tag = component.string
    if tag == 'swan.navigateT':
        tag = 'swan.navigateTo'
    link = component.get('href')
    # 替换错误链接
    if link.startswith('http://localhost:4000/'):
        link = link.replace('http://localhost:4000/', 'https://smartprogram.baidu.com/')
    td = component.parent.find_next_sibling('td')
    if not td:
        continue
    desc = td.string
    api_json['api'] = tag
    api_json['desc'] = desc
    api_json['link'] = link
    print link
    # 遍历控件属性
    component_attr_content = requests.get(link).content
    attr_soup = bs4.BeautifulSoup(component_attr_content, "lxml")
    class_name = tag.replace('.', '-').strip()
    print class_name
    view_header = attr_soup.find(id=class_name)
    attr_array = []
    table = view_header.find_next_sibling('table')
    if table:
        tr_list = table.select('tbody>tr')
        for i in tr_list:
            attr_item = {}
            for j, value in enumerate(i.find_all('td')):
                if j == 0:
                    attr_item['name'] = value.get_text()
                elif j == 1:
                    attr_item['type'] = value.get_text()
                elif j == 2:
                    attr_item['required'] = value.get_text()
                elif j == 3:
                    attr_item['default'] = value.get_text()
                elif j == 3:
                    attr_item['desc'] = parser.unescape(value.get_text())
            attr_array.append(attr_item)
    api_json['attrs'] = attr_array
    output.append(api_json)
    print api_json
    print '----------------'
print '已完成抓取{}个控件'.format(len(api_tags))
# 写入到文件
fo = open("api_list.json", "w")
fo.write(json.dumps(output))
fo.close()
