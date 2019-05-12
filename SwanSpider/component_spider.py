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
                attr_item['valuePattern'] = None
                type_text = value.get_text()
                type_value = 'Any'
                if "Number" == type_text or "Float" == type_text:
                    type_value = 'Number'
                elif "Boolean" == type_text:
                    type_value = 'Boolean'
                elif "String" == type_text:
                    type_value = 'String'
                elif "EventHandle" == type_text or "Event" == type_text or "Handle" == type_text:
                    type_value = 'EventHandle'
                elif "Color" == type_text:
                    type_value = 'Color'
                elif "Array" == type_text or "NumberArray" == type_text or "Array / String" == type_text:
                    type_value = 'Array'
                attr_item['valueType'] = type_value
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
# 官方未收录的组件
un_contain = [
    {
        "tag": "block",
        "desc": "block component",
        "link": "https://smartprogram.baidu.com/docs/develop/component/list/",
        "child": [],
        "parent": [],
        "attrs": []
    },
    {
        "tag": "checkbox-group",
        "desc": "checkbox-group component",
        "link": "https://smartprogram.baidu.com/docs/develop/component/list/",
        "child": ["checkbox"],
        "parent": [],
        "attrs": [
            {
                "defaultValue": "",
                "valueType": "function",
                "valuePattern": "",
                "name": "bindchange",
                "desc": "<checkbox-group/>中选中项发生改变时触发 change 事件"
            }
        ]
    },
    {
        "tag": "radio-group",
        "desc": "radio-group component",
        "link": "https://smartprogram.baidu.com/docs/develop/component/list/",
        "child": ["radio"],
        "parent": [],
        "attrs": [
            {
                "defaultValue": "",
                "valueType": "function",
                "valuePattern": "",
                "name": "bindchange",
                "desc": "<radio-group/>中选中项发生改变时触发 change 事件"
            }
        ]
    },
    {
        "tag": "swiper-item",
        "desc": "swiper-item component",
        "link": "https://smartprogram.baidu.com/docs/develop/component/list/",
        "child": [],
        "parent": ["swiper"],
        "attrs": [
            {
                "defaultValue": "",
                "valueType": "string",
                "valuePattern": "",
                "name": "item-id",
                "desc": "该swiper-item的标识符"
            }
        ]
    }
]
output.extend(un_contain)

# 写入到文件
fo = open("component_list.json", "w")
fo.write(json.dumps(output))
fo.close()
