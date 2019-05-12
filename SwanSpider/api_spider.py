# coding:utf-8

import bs4
import requests
import HTMLParser
import json
from xml.dom.minidom import Document

# 写入xml
doc = Document()
templateSet = doc.createElement('templateSet')
templateSet.setAttribute('group', 'SwanGroup')
doc.appendChild(templateSet)

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
                    attr_item['required'] = True if value.get_text() == u'是' else False
                elif j == 3:
                    attr_item['default'] = value.get_text()
                elif j == 4:
                    attr_item['desc'] = parser.unescape(value.get_text())
            attr_array.append(attr_item)
    api_json['attrs'] = attr_array
    output.append(api_json)
    print api_json

    # 生成 xml
    template = doc.createElement('template')
    template.setAttribute('name', tag[5:])
    code_holder = tag
    print attr_array
    br = '\n'
    if len(attr_array) == 0:
        code_holder += '();&#10;'
    else:
        code_holder += '({&#10;'
        for argument in attr_array:
            if not argument.has_key('name') or not argument.has_key('type'):
                continue
            name = argument['name']
            if name == 'success':
                code_holder += '    success: res =&gt; {&#10;    },&#10;'
            if name == 'fail':
                code_holder += '    fail: res =&gt; {&#10;    },&#10;'
            else:
                if not argument.has_key('required') or not argument['required']:
                    continue
                code_holder += '    {}: null,&#10;'.format(name)
        code_holder += '});'
    template.setAttribute('value', code_holder)
    template.setAttribute('toReformat', "false")
    template.setAttribute('toShortenFQNames', "true")
    template.setAttribute('description', desc)
    templateSet.appendChild(template)
    context = doc.createElement('context')
    options = ["JAVA_SCRIPT", "JS_EXPRESSION", "JSX_HTML", "JS_STATEMENT"]
    for i in options:
        option = doc.createElement('option')
        option.setAttribute("name", i)
        option.setAttribute("value", "true")
        context.appendChild(option)
    template.appendChild(context)
    print '----------------'
print '已完成抓取{}个API'.format(len(api_tags))
# 写入 json 到文件
fo = open("api_list.json", "w")
fo.write(json.dumps(output))
fo.close()

# 写入 xml 到文件
with open("api_list.xml", 'w') as f:
    f.write(doc.toprettyxml(indent='\t', encoding='utf-8'))
