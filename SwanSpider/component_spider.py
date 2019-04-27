# coding:utf-8

import bs4
import requests
import HTMLParser
import json

parser = HTMLParser.HTMLParser()
# 遍历控件列表
content = requests.get("https://smartprogram.baidu.com/docs/develop/component/list/").content
soup = bs4.BeautifulSoup(content, "lxml")
component_tags = soup.select('table td>a')
output = []
for component in component_tags:
    component_json = {}
    tag = component.string
    link = component.get('href')
    desc = component.parent.find_next_sibling('td').string
    component_json['tag'] = tag
    component_json['desc'] = desc

    component_json['parent'] = []
    component_json['child'] = []
    # 替换错误链接
    if link.startswith('hhttps://'):
        link = link.replace('hhttps://', 'https://')
    component_json['link'] = link
    # 遍历控件属性
    component_attr_content = requests.get(link).content
    attr_soup = bs4.BeautifulSoup(component_attr_content, "lxml")
    view_header = attr_soup.find(id=tag)
    tr_list = view_header.find_next_sibling('table').select('tbody>tr')
    attr_array = []
    for i in tr_list:
        attr_item = {}
        for j, value in enumerate(i.find_all('td')):
            if j == 0:
                attr_item['name'] = value.get_text()
            elif j == 1:
                attr_item['valueType'] = value.get_text()
                attr_item['valuePattern'] = None
            elif j == 2:
                attr_item['defaultValue'] = value.get_text()
            elif j == 3:
                attr_item['desc'] = parser.unescape(value.get_text())
        attr_array.append(attr_item)
    component_json['attrs'] = attr_array
    output.append(component_json)
    print component_json
    print '----------------'
print '已完成抓取{}个控件'.format(len(component_tags))
# 写入到文件
fo = open("component_list.json", "w")
fo.write(json.dumps(output))
fo.close()
